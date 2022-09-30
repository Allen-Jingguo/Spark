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
  Upload,
  PlusOutlined,
} from 'antd';
import React, { useState, useEffect } from 'react';
import keyMapperApi from '@/services/spark/keyMapperApi';

const KeyMapperCreateModal = (props) => {
  const [confirmLoading, setConfirmLoading] = useState(false);

  const [allType, setAllType] = useState([]);

  const [form] = Form.useForm();

  const handleOk = () => {
    form.submit();
  };

  const handleCancel = () => {
    props.closeCreateModal();
  };

  useEffect(() => {
    keyMapperApi.getAllFormatterType().then((data) => {
      setAllType(data);
    });
  }, []);

  const doCreateNew = (values) => {
    setConfirmLoading(true);

    keyMapperApi.createNew({ ...values }).then((data) => {
      if (data) {
        message.success('operate successfully!');
        props.closeCreateModal();
      }
      setConfirmLoading(false);
    });
  };

  return (
    <>
      <Modal
        title="Create New Mapper"
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
              name="keyMapperType"
              label="Type"
              rules={[{ required: true, message: 'Missing Outbound Type' }]}
            >
              <Select
                options={[{ label: 'Key-value', value: 'Key-value' }, ...allType]}
                style={{ width: 400 }}
                onChange={(value) => {}}
              />
            </Form.Item>

            <Form.Item
              name="properties"
              label="Mapper Template ( Complex  templates will be supported in the future )"
            >
              <Input.TextArea
                style={{ width: 400, height: 200 }}
                showCount
                placeholder="input  template text "
              />
              <div style={{ fontSize: 15, marginTop: 10 }}> OR </div>
            </Form.Item>

            <Form.Item label="Upload" valuePropName="fileList">
              <Upload action="/api/formatter/upload" listType="picture-card">
                <div>
                  {/* <PlusOutlined /> */}
                  <div
                    style={{
                      marginTop: 8,
                    }}
                  >
                    Upload
                  </div>
                </div>
              </Upload>
            </Form.Item>
          </Form>
        </div>
      </Modal>
    </>
  );
};

export default KeyMapperCreateModal;
