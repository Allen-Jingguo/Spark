import {
  Space,
  Table,
  Tag,
  Button,
  Col,
  Form,
  Input,
  Row,
  Select,
  UpOutlined,
  DownOutlined,
  message,
  Popconfirm,
  Modal,
  TextArea,
  Radio,
} from 'antd';
import React, { useState, useEffect } from 'react';
import inboundApi from '@/services/spark/inboundApi';

const InboundCreateModal = (props) => {
  const [confirmLoading, setConfirmLoading] = useState(false);

  const [allInboundType, setAllInboundType] = useState([]);

  const [selectInboundType, setSelectInboundType] = useState();

  const [form] = Form.useForm();

  const handleOk = () => {
    form.submit();
  };

  const handleCancel = () => {
    props.closeCreateModal();
  };

  useEffect(() => {
    inboundApi.getAllInboundType().then((resp) => {
      if (resp.success) {
        setAllInboundType(resp.data);
      }
    });
  }, []);

  const doCreateNew = (values) => {
    setConfirmLoading(true);

    inboundApi.createNew({ ...values }).then((resp) => {
      if (resp.success) {
        message.success('operate successfully!');
        props.closeCreateModal(true);
      }
      setConfirmLoading(false);
    });
  };

  return (
    <>
      <Modal
        title="Create New Inbound"
        //open={open}
        visible={true}
        onOk={handleOk}
        confirmLoading={confirmLoading}
        onCancel={handleCancel}
      >
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <Form
            form={form}
            name="advanced_search"
            className="ant-advanced-search-form"
            layout="vertical"
            onFinish={doCreateNew}
            labelAlign="right"
          >
            <Form.Item
              name="name"
              label="Name (unique)"
              rules={[{ required: true, message: 'Missing  Name' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="inboundType"
              label="Inbound Type"
              rules={[{ required: true, message: 'Missing Outbound Type' }]}
            >
              <Select
                options={allInboundType}
                style={{ width: 400 }}
                onChange={(value) => {
                  setSelectInboundType(value);
                }}
              />
            </Form.Item>

            <Form.Item
              name="server"
              label="Host Name"
              rules={[{ required: true, message: 'Missing Host Name' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="port"
              label="Port"
              rules={[{ required: true, message: 'Missing Port' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="userName"
              label="User Name"
              rules={[{ required: true, message: 'Missing User Name' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="password"
              label="Password"
              rules={[{ required: true, message: 'Missing Password' }]}
            >
              <Input.Password style={{ width: 400 }} />
            </Form.Item>

            {selectInboundType === 'JDBC' ? (
              <>
                <Form.Item
                  name="jdbcType"
                  label="JDBC Type"
                  rules={[{ required: true, message: 'Missing JDBC Type' }]}
                >
                  <Select
                    options={[
                      { label: 'Oracle', value: 'Oracle' },
                      { label: 'Mysql', value: 'Mysql' },
                    ]}
                    style={{ width: 400 }}
                  />
                </Form.Item>
                <Form.Item
                  name="executeSql"
                  label="Execute sql (More SQL will be supported in the future)"
                  rules={[{ required: true, message: 'Missing User Name' }]}
                >
                  <Input.TextArea
                    style={{ width: 400, height: 150 }}
                    showCount
                    placeholder="input other properties"
                  />
                </Form.Item>
                <Form.Item
                  name="timer"
                  label="Execute Timer (More timers are supported in the future)"
                  rules={[{ required: true, message: 'Missing User Name' }]}
                >
                  <Radio.Group>
                    <Radio value="once">once</Radio>
                    <Radio value="loop">loop</Radio>
                  </Radio.Group>
                </Form.Item>
              </>
            ) : (
              ''
            )}

            {selectInboundType === 'KAFKA' ? (
              <Form.Item
                name="topic"
                label="Topic Name"
                rules={[{ required: true, message: 'Missing Topic Name' }]}
              >
                <Input style={{ width: 400 }} />
              </Form.Item>
            ) : (
              ''
            )}

            <Form.Item
              name="properties"
              label="Properties (key=value , Used to initialize inbound )"
            >
              <Input.TextArea
                style={{ width: 400, height: 200 }}
                showCount
                placeholder="input other properties"
              />
            </Form.Item>

            <Button type="primary">Test Connection</Button>
          </Form>
        </div>
      </Modal>
    </>
  );
};

export default InboundCreateModal;
